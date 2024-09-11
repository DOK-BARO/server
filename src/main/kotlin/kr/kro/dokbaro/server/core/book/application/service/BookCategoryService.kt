package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCategoryPort
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import org.springframework.stereotype.Service

@Service
class BookCategoryService(
	private val readBookCategoryPort: ReadBookCategoryPort,
) : FindAllBookCategoryUseCase {
	override fun findAllCategory(id: Long?): BookCategory =
		readBookCategoryPort.getBookCategory(id ?: BookCategory.ROOT_ID)
}