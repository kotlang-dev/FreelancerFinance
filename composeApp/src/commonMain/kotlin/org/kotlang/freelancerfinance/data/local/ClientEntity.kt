package org.kotlang.freelancerfinance.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val gstin: String?,
    val address: String,
    val stateCode: Int,
    val email: String?
)