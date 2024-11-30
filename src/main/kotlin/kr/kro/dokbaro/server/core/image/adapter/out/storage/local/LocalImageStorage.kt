package kr.kro.dokbaro.server.core.image.adapter.out.storage.local

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Component
class LocalImageStorage(
	@Value("\${local.image.storage.host-url}") val hostUrl: String,
	@Value("\${local.image.storage.location-path}") val locationPath: String,
	private val fileNameStrategy: FileNameStrategy,
) {
	fun save(
		file: MultipartFile,
		middlePath: String,
	): String {
		val fileName =
			fileNameStrategy.generateName(
				originName =
					file.originalFilename
						?: throw InvalidFileNameException(file.originalFilename),
			)

		val totalPath =
			combinePath(
				rootPath = locationPath,
				middlePath = middlePath,
			)
		val newFile = File(totalPath, fileName)
		newFile.mkdirs()
		file.transferTo(newFile)

		return hostUrl + totalPath + fileName
	}

	private fun combinePath(
		rootPath: String,
		middlePath: String,
	): String {
		val combined = rootPath.trimEnd('/') + "/" + middlePath.trimStart('/')
		if (combined.endsWith("/")) {
			return combined
		}
		return "$combined/"
	}

	fun load(locationPath: String): File = File(locationPath)
}