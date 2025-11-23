package org.kotlang.freelancerfinance

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform