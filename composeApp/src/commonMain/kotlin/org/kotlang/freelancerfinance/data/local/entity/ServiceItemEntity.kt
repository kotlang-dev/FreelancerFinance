package org.kotlang.freelancerfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_items")
data class ServiceItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val defaultPrice: Double,
    val taxRate: Double = 0.0,
    val hsnSacCode: String?
)