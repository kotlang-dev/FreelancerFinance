package org.kotlang.freelancerfinance.data.mapper

import org.kotlang.freelancerfinance.data.local.entity.ServiceItemEntity
import org.kotlang.freelancerfinance.domain.model.ServiceItem

fun ServiceItemEntity.toDomain(): ServiceItem {
    return ServiceItem(
        id = id,
        name = name,
        description = description,
        defaultPrice = defaultPrice,
        taxRate = taxRate,
        hsnSacCode = hsnSacCode
    )
}

fun ServiceItem.toEntity(): ServiceItemEntity {
    return ServiceItemEntity(
        id = id,
        name = name,
        description = description,
        defaultPrice = defaultPrice,
        taxRate = taxRate,
        hsnSacCode = hsnSacCode
    )
}