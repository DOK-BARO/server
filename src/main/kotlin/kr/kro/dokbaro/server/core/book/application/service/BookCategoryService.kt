package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.core.book.application.port.input.CreateBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCategoryCommand
import kr.kro.dokbaro.server.core.book.application.port.out.InsertBookCategoryPort
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import org.springframework.stereotype.Service

@Service
class BookCategoryService(
	private val insertBookCategoryPort: InsertBookCategoryPort,
) : CreateBookCategoryUseCase {
	override fun create(command: CreateBookCategoryCommand): Long =
		insertBookCategoryPort.insert(
			BookCategory(
				koreanName = command.koreanName,
				englishName = command.englishName,
				parentId = command.parentId,
			),
		)
}