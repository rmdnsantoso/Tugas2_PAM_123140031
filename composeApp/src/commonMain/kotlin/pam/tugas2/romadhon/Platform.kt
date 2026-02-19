package pam.tugas2.romadhon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform