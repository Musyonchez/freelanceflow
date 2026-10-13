package com.daleen.freelanceflow.data.local

import androidx.room.TypeConverter
import com.daleen.freelanceflow.data.local.entity.ClientStatus
import com.daleen.freelanceflow.data.local.entity.InvoiceStatus
import com.daleen.freelanceflow.data.local.entity.ProjectStatus
import com.daleen.freelanceflow.data.local.entity.RateType

class Converters {
    @TypeConverter
    fun fromClientStatus(value: ClientStatus): String = value.name

    @TypeConverter
    fun toClientStatus(value: String): ClientStatus = ClientStatus.valueOf(value)

    @TypeConverter
    fun fromProjectStatus(value: ProjectStatus): String = value.name

    @TypeConverter
    fun toProjectStatus(value: String): ProjectStatus = ProjectStatus.valueOf(value)

    @TypeConverter
    fun fromRateType(value: RateType): String = value.name

    @TypeConverter
    fun toRateType(value: String): RateType = RateType.valueOf(value)

    @TypeConverter
    fun fromInvoiceStatus(value: InvoiceStatus): String = value.name

    @TypeConverter
    fun toInvoiceStatus(value: String): InvoiceStatus = InvoiceStatus.valueOf(value)
}
