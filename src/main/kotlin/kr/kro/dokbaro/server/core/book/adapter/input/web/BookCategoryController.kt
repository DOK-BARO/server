package kr.kro.dokbaro.server.core.book.adapter.input.web

import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book-categories")
class BookCategoryController(
	private val findAllBookCategoryUseCase: FindAllBookCategoryUseCase,
) {
	@GetMapping
	fun getCategories(
		@RequestParam(required = false) targetId: Long?,
	): BookCategory = findAllBookCategoryUseCase.findAllCategory(targetId)
}