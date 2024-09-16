package kr.kro.dokbaro.server.core.book.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.core.book.application.port.input.CreateBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindOneBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCommand
import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(
	private val createBookUseCase: CreateBookUseCase,
	private val findAllBookUseCase: FindAllBookUseCase,
	private val findOneBookUseCase: FindOneBookUseCase,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(
		@RequestBody body: CreateBookCommand,
	): IdResponse<Long> = IdResponse(createBookUseCase.create(body))

	@GetMapping
	fun findAll(
		@RequestParam title: String?,
		@RequestParam authorName: String?,
		@RequestParam description: String?,
		@RequestParam category: Long?,
		@RequestParam(defaultValue = "1") page: Long,
		@RequestParam size: Long,
	): Collection<BookSummary> =
		findAllBookUseCase
			.findAllBy(FindAllBookCommand(title, authorName, description, category, page, size))

	@GetMapping("/{id}")
	fun findOne(
		@PathVariable id: Long,
	): BookDetail = findOneBookUseCase.getBy(id)
}