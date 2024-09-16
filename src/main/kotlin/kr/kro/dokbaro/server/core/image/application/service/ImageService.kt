package kr.kro.dokbaro.server.core.image.application.service

import kr.kro.dokbaro.server.core.image.application.port.input.FindImageUseCase
import kr.kro.dokbaro.server.core.image.application.port.input.UploadImageUseCase
import kr.kro.dokbaro.server.core.image.application.port.out.InsertImagePort
import kr.kro.dokbaro.server.core.image.application.port.out.LoadImagePort
import kr.kro.dokbaro.server.core.image.domain.ImageTarget
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class ImageService(
	private val insertImagePort: InsertImagePort,
	private val loadImagePort: LoadImagePort,
) : UploadImageUseCase,
	FindImageUseCase {
	override fun upload(
		file: MultipartFile,
		target: String,
	): String = insertImagePort.insert(file, ImageTarget.valueOf(target).path)

	override fun findBy(path: String): File = loadImagePort.load(path)
}