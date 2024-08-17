package kr.kro.dokbaro.server.core.book.adapter.input.web

import kr.kro.dokbaro.server.core.book.adapter.input.web.dto.BookResponse
import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.application.port.input.query.FindAllBookUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(
	private val findAllBookUseCase: FindAllBookUseCase,
) {
	@GetMapping
	fun findAll(
		@RequestParam title: String?,
		@RequestParam authorName: String?,
		@RequestParam description: String?,
		@RequestParam category: Long?,
		@RequestParam lastId: Long?,
		@RequestParam limit: Long,
	): Collection<BookResponse> =
		findAllBookUseCase
			.findBy(FindAllBookCommand(title, authorName, description, category, lastId, limit))
			.map { BookResponse(it) }
}