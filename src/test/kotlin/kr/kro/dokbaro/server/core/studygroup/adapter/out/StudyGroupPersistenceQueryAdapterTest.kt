package kr.kro.dokbaro.server.core.studygroup.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq.StudyGroupMapper
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupQueryRepository
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class StudyGroupPersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val studyGroupRepository = StudyGroupRepository(dslContext, StudyGroupMapper())

		val queryAdapter = StudyGroupPersistenceQueryAdapter(StudyGroupQueryRepository(dslContext, StudyGroupMapper()))
		"member가 소속된 study group 목록을 조회한다" {
			val members =
				listOf(
					memberRepository.insert(memberFixture(email = Email("aaa@aa.com"))),
					memberRepository.insert(memberFixture(email = Email("bbb@aa.com"))),
					memberRepository.insert(memberFixture(email = Email("ccc@aa.com"))),
				)

			listOf(
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(members[0].id, StudyMemberRole.LEADER),
						),
				),
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(members[1].id, StudyMemberRole.LEADER),
							StudyMember(members[0].id, StudyMemberRole.MEMBER),
						),
				),
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(members[1].id, StudyMemberRole.LEADER),
							StudyMember(members[2].id, StudyMemberRole.MEMBER),
						),
				),
			).forEach { studyGroupRepository.insert(it) }

			queryAdapter.findAllByStudyMemberId(members[0].id).size shouldBe 2
		}

		"study group에 소속된 member 목록을 조회한다" {
			val members =
				listOf(
					memberRepository.insert(memberFixture(email = Email("aaa@aa.com"))),
					memberRepository.insert(memberFixture(email = Email("bbb@aa.com"))),
					memberRepository.insert(memberFixture(email = Email("ccc@aa.com"))),
				)

			val groupId =
				studyGroupRepository.insert(
					studyGroupFixture(
						studyMembers =
							mutableSetOf(
								StudyMember(members[0].id, StudyMemberRole.LEADER),
								StudyMember(members[1].id, StudyMemberRole.MEMBER),
								StudyMember(members[2].id, StudyMemberRole.MEMBER),
							),
					),
				)

			queryAdapter.findAllStudyGroupMembers(groupId).size shouldBe 3
		}
	})