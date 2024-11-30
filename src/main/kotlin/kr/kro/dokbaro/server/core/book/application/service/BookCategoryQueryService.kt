package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCategoryPort
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree
import org.springframework.stereotype.Service

@Service
class BookCategoryQueryService(
	private val readBookCategoryPort: ReadBookCategoryPort,
) : FindAllBookCategoryUseCase {
	override fun getTree(id: Long?): BookCategoryTree = readBookCategoryPort.findTreeBy(id = id ?: BookCategory.ROOT_ID)
}