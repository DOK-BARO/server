package kr.kro.dokbaro.server.core.image.application.port.input

import java.io.File

fun interface FindImageUseCase {
	fun findBy(path: String): File
}