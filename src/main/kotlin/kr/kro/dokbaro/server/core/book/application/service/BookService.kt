package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.core.book.application.port.input.CreateBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCommand
import kr.kro.dokbaro.server.core.book.application.port.out.InsertBookPort
import kr.kro.dokbaro.server.core.book.application.service.auth.BookAuthorityCheckService
import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookAuthor
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.stereotype.Service

@Service
class BookService(
	private val insertBookPort: InsertBookPort,
	private val bookAuthorityCheckService: BookAuthorityCheckService,
) : CreateBookUseCase {
	override fun create(
		command: CreateBookCommand,
		user: DokbaroUser,
	): Long {
		bookAuthorityCheckService.checkCreateBookAuthority(user)

		return insertBookPort.insert(
			Book(
				isbn = command.isbn,
				title = command.title,
				publisher = command.publisher,
				publishedAt = command.publishedAt,
				price = command.price,
				description = command.description,
				imageUrl = command.imageUrl,
				categories = command.categories,
				authors =
					command.authors.map
						{ BookAuthor(it) },
			),
		)
	}
}