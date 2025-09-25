package com.example.sports.data.remote.datasource

import com.example.sports.data.remote.dto.SportsPerformanceDto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseSportsPerformanceDataSource {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val performancesRef = database.child("sports_performances")

    suspend fun insertPerformance(performance: SportsPerformanceDto): String {
        return try {
            val key = performancesRef.push().key ?: throw Exception("Failed to generate key")
            val performanceWithId = performance.copy(id = key)
            performancesRef.child(key).setValue(performanceWithId).await()
            Timber.d("Performance inserted successfully with key: $key")
            key
        } catch (e: Exception) {
            when (e.message?.contains("Permission denied")) {
                true -> {
                    Timber.e("Firebase permission denied while inserting performance. Check database security rules.")
                    throw SecurityException("Permission denied: Check Firebase database security rules", e)
                }
                else -> {
                    Timber.e(e, "Failed to insert performance")
                    throw e
                }
            }
        }
    }

    fun getAllPerformances(): Flow<List<SportsPerformanceDto>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val performances = mutableListOf<SportsPerformanceDto>()
                    for (childSnapshot in snapshot.children) {
                        try {
                            val performance = childSnapshot.getValue(SportsPerformanceDto::class.java)
                            performance?.let { performances.add(it) }
                        } catch (e: Exception) {
                            Timber.e(e, "Failed to parse performance: ${childSnapshot.key}")
                        }
                    }
                    trySend(performances.sortedByDescending { it.createdAt })
                } catch (e: Exception) {
                    Timber.e(e, "Failed to process data changes")
                    trySend(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                when (error.code) {
                    DatabaseError.PERMISSION_DENIED -> {
                        Timber.e("Firebase permission denied. Check database security rules.")
                        trySend(emptyList())
                        close()
                    }
                    DatabaseError.NETWORK_ERROR -> {
                        Timber.e("Network error accessing Firebase: ${error.message}")
                        trySend(emptyList())
                        close()
                    }
                    else -> {
                        Timber.e("Database error: ${error.message} (Code: ${error.code})")
                        trySend(emptyList())
                        close(error.toException())
                    }
                }
            }
        }

        try {
            performancesRef.addValueEventListener(listener)
        } catch (e: Exception) {
            Timber.e(e, "Failed to add Firebase listener")
            trySend(emptyList())
            close(e)
        }

        awaitClose {
            try {
                performancesRef.removeEventListener(listener)
            } catch (e: Exception) {
                Timber.e(e, "Failed to remove Firebase listener")
            }
        }
    }

    suspend fun deletePerformance(performanceId: String) {
        try {
            performancesRef.child(performanceId).removeValue().await()
            Timber.d("Performance deleted successfully: $performanceId")
        } catch (e: Exception) {
            when (e.message?.contains("Permission denied")) {
                true -> {
                    Timber.e("Firebase permission denied while deleting performance. Check database security rules.")
                    throw SecurityException("Permission denied: Check Firebase database security rules", e)
                }
                else -> {
                    Timber.e(e, "Failed to delete performance: $performanceId")
                    throw e
                }
            }
        }
    }

    suspend fun deleteAllPerformances() {
        try {
            performancesRef.removeValue().await()
            Timber.d("All performances deleted successfully")
        } catch (e: Exception) {
            when (e.message?.contains("Permission denied")) {
                true -> {
                    Timber.e("Firebase permission denied while deleting all performances. Check database security rules.")
                    throw SecurityException("Permission denied: Check Firebase database security rules", e)
                }
                else -> {
                    Timber.e(e, "Failed to delete all performances")
                    throw e
                }
            }
        }
    }
}