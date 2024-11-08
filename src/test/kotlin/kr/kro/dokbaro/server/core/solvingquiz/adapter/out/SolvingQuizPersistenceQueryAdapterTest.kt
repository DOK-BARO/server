package kr.kro.dokbaro.server.core.solvingquiz.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.entity.jooq.SolvingQuizMapper
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizQueryRepository
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizRepository
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq.StudyGroupMapper
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class SolvingQuizPersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))
		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val bookRepository = BookRepository(dslContext)
		val bookQuizRepository = BookQuizRepository(dslContext, BookQuizMapper())
		val studyGroupRepository = StudyGroupRepository(dslContext, StudyGroupMapper())

		val solvingQuizRepository = SolvingQuizRepository(dslContext, SolvingQuizMapper())
		val solvingQuizQueryRepository = SolvingQuizQueryRepository(dslContext, SolvingQuizMapper())

		val adapter = SolvingQuizPersistenceQueryAdapter(solvingQuizQueryRepository)

		"내가 푼 퀴즈 목록을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val member2Id = memberRepository.insert(memberFixture(email = Email("sub@gmail.com"))).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			solvingQuizRepository.insert(SolvingQuiz(playerId = memberId, quizId = bookQuizId))
			solvingQuizRepository.insert(SolvingQuiz(playerId = member2Id, quizId = bookQuizId))

			adapter.findAllMySolveSummary(memberId).size shouldBe 1
		}

		"그룹 내 내가 푼 퀴즈 목록을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val studyGroupId =
				studyGroupRepository.insert(
					studyGroupFixture(
						studyMembers = mutableSetOf(StudyMember(memberId, StudyMemberRole.LEADER)),
					),
				)
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId =
				bookQuizRepository.insert(
					bookQuizFixture(
						creatorId = memberId,
						bookId = bookId,
						studyGroupId = studyGroupId,
					),
				)

			solvingQuizRepository.insert(SolvingQuiz(playerId = memberId, quizId = bookQuizId))

			adapter.findAllMyStudyGroupSolveSummary(memberId, studyGroupId).shouldNotBeEmpty()
		}
	})