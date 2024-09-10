package kr.kro.dokbaro.server.core.bookquiz.application.service

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.CreateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.SaveBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import org.springframework.stereotype.Service

@Service
class BookQuizService(
	private val saveBookQuizPort: SaveBookQuizPort,
	private val findCertificatedMemberUseCase: FindCertificatedMemberUseCase,
) : CreateBookQuizUseCase {
	override fun create(command: CreateBookQuizCommand): Long {
		val loginUser = findCertificatedMemberUseCase.getByCertificationId(command.creatorAuthId)

		return saveBookQuizPort.save(
			BookQuiz(
				title = command.title,
				description = command.description,
				bookId = command.bookId,
				creatorMemberId = loginUser.id,
			),
		)
	}
}