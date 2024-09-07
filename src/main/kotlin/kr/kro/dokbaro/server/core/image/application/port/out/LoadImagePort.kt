package kr.kro.dokbaro.server.core.image.application.port.out

import java.io.File

fun interface LoadImagePort {
	fun load(locationPath: String): File
}