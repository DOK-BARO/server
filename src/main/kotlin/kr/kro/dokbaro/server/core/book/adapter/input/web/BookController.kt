package kr.kro.dokbaro.server.core.book.adapter.input.web

import kr.kro.dokbaro.server.core.book.adapter.input.web.dto.BookResponse
import kr.kro.dokbaro.server.core.book.adapter.input.web.dto.BookSummary
import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.application.port.input.query.FindAllBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.query.FindOneBookUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(
	private val findAllBookUseCase: FindAllBookUseCase,
	private val findOneBookUseCase: FindOneBookUseCase,
) {
	@GetMapping
	fun findAll(
		@RequestParam title: String?,
		@RequestParam authorName: String?,
		@RequestParam description: String?,
		@RequestParam category: Long?,
		@RequestParam lastId: Long?,
		@RequestParam limit: Long,
	): Collection<BookSummary> =
		findAllBookUseCase
			.findAllBy(FindAllBookCommand(title, authorName, description, category, lastId, limit))
			.map { BookSummary(it) }

	@GetMapping("/{id}")
	fun findOne(
		@PathVariable id: Long,
	): BookResponse = BookResponse(findOneBookUseCase.findById(id))
}