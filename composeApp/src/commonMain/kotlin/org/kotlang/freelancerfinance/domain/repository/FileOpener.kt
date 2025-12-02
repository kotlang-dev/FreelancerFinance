package org.kotlang.freelancerfinance.domain.repository

interface FileOpener {
    fun openFile(path: String)
}