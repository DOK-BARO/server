package kr.kro.dokbaro.server.core.solvingquiz.application.service.auth

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadStudyGroupMemberIdsCollectionByQuizIdPort
import kr.kro.dokbaro.server.core.solvingquiz.application.service.exception.StudyQuizForbiddenException

class SolvingQuizAuthorityCheckServiceTest :
	StringSpec({

		val readStudyGroupMemberIdsCollectionByQuizIdPort: ReadStudyGroupMemberIdsCollectionByQuizIdPort = mockk()
		val solvingQuizAuthorityCheckService =
			SolvingQuizAuthorityCheckService(
				readStudyGroupMemberIdsCollectionByQuizIdPort,
			)

		"스터디 그룹에 속하지 않으면 예외를 반환한다" {
			every { readStudyGroupMemberIdsCollectionByQuizIdPort.findAllGroupMemberIdsByQuizId(any()) } returns
				listOf(1, 2)

			shouldThrow<StudyQuizForbiddenException> {
				solvingQuizAuthorityCheckService.checkSolvingQuiz(playerId = 3, quizId = 10)
			}
		}
		
		"스터디 그룹에 속해 있으면 퀴즈를 풀 수 있다." {
			every { readStudyGroupMemberIdsCollectionByQuizIdPort.findAllGroupMemberIdsByQuizId(any()) } returns
				listOf(1, 2)

			shouldNotThrow<StudyQuizForbiddenException> {
				solvingQuizAuthorityCheckService.checkSolvingQuiz(playerId = 2, quizId = 10)
			}
		}
	})