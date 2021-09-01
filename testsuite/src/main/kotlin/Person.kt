package com.github.mvysny.kaributools

import java.io.Serializable
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Size

data class Person(@field:NotNull
                  @field:Size(min = 3, max = 200)
                  var fullName: String? = null,

                  @field:NotNull
                  @field:PastOrPresent
                  var dateOfBirth: LocalDate? = null,

                  @field:NotNull
                  var alive: Boolean = false,

                  var testBoolean: Boolean? = null,

                  var comment: String? = null,

                  var testDouble: Double? = null,

                  var testInt: Int? = null,

                  var testLong: Long? = null,

                  var testBD: BigDecimal? = null,

                  var testBI: BigInteger? = null,

                  var created: Instant? = null,

                  var testCalendar: Calendar? = null
) : Serializable
