package kr.kro.dokbaro.server.core.book.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.core.book.application.port.input.CreateBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCategoryCommand
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book-categories")
class BookCategoryController(
	private val createBookCategoryUseCase: CreateBookCategoryUseCase,
	private val findAllBookCategoryUseCase: FindAllBookCategoryUseCase,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createBookCategory(
		@RequestBody body: CreateBookCategoryCommand,
	): IdResponse<Long> = IdResponse(createBookCategoryUseCase.create(body))

	@GetMapping
	fun getCategories(
		@RequestParam(required = false) targetId: Long?,
	): BookCategoryTree = findAllBookCategoryUseCase.getTree(targetId)
}