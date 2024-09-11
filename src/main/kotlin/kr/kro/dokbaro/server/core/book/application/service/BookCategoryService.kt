package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookCategoryPort
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import org.springframework.stereotype.Service

@Service
class BookCategoryService(
	private val loadBookCategoryPort: LoadBookCategoryPort,
) : FindAllBookCategoryUseCase {
	override fun findAllCategory(id: Long?): BookCategory =
		loadBookCategoryPort.getBookCategory(id ?: BookCategory.ROOT_ID)
}