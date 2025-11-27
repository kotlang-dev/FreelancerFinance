package org.kotlang.freelancerfinance.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import org.kotlang.freelancerfinance.data.local.dao.ClientDao
import org.kotlang.freelancerfinance.data.local.dao.InvoiceDao
import org.kotlang.freelancerfinance.data.local.entity.ClientEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceItemEntity

@Database(
    entities = [
        ClientEntity::class,
        InvoiceEntity::class,
        InvoiceItemEntity::class
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val clientDao: ClientDao
    abstract val invoiceDao: InvoiceDao
}