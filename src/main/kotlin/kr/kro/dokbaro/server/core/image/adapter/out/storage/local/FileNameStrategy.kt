package kr.kro.dokbaro.server.core.image.adapter.out.storage.local

fun interface FileNameStrategy {
	fun generateName(originName: String): String
}