package kr.kro.dokbaro.server.core.studygroup.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq.StudyGroupMapper
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupQueryRepository
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.application.port.out.dto.CountStudyGroupCondition
import kr.kro.dokbaro.server.core.studygroup.application.port.out.dto.FindStudyGroupCondition
import kr.kro.dokbaro.server.core.studygroup.domain.InviteCode
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.core.studygroup.query.sort.MyStudyGroupSortKeyword
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture
import org.jooq.DSLContext
import java.util.UUID

@PersistenceAdapterTest
class StudyGroupPersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val studyGroupRepository = StudyGroupRepository(dslContext)

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

			queryAdapter.findAllByStudyMemberId(members[0].id, PageOption.of()).size shouldBe 2
			queryAdapter
				.findAllByStudyMemberId(
					members[0].id,
					PageOption.of(sort = MyStudyGroupSortKeyword.NAME),
				).size shouldBe
				2
			queryAdapter
				.findAllByStudyMemberId(
					members[0].id,
					PageOption.of(sort = MyStudyGroupSortKeyword.JOINED_AT),
				).size shouldBe
				2
			queryAdapter
				.findAllByStudyMemberId(
					members[0].id,
					PageOption.of(direction = SortDirection.DESC),
				).size shouldBe
				2
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

		"스터디 그룹 정보를 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id

			val groupId: Long =
				studyGroupRepository.insert(
					studyGroupFixture(
						studyMembers =
							mutableSetOf(
								StudyMember(memberId, StudyMemberRole.LEADER),
							),
					),
				)

			queryAdapter.findStudyGroupDetailBy(FindStudyGroupCondition(id = groupId)) shouldNotBe null
		}

		"초대 코드를 통한 검색을 수행한다" {
			val savedMember =
				memberRepository.insert(
					Member(
						nickname = "nick",
						email = Email("www@gmail.com"),
						profileImage = "aaa.png",
						certificationId = UUID.randomUUID(),
					),
				)
			val targetCode = "ABC123"

			val studyGroup =
				studyGroupFixture(
					studyMembers = mutableSetOf(StudyMember(savedMember.id, StudyMemberRole.LEADER)),
					inviteCode = InviteCode(targetCode),
					introduction = null,
				)
			studyGroupRepository.insert(studyGroup)

			queryAdapter.findBy(FindStudyGroupCondition(inviteCode = targetCode)) shouldNotBe null
			queryAdapter.findBy(FindStudyGroupCondition(inviteCode = "123ABC")) shouldBe null
		}

		"스터디 그룹 개수를 조회한다" {
			val member1 = memberRepository.insert(memberFixture(email = Email("aa1@gmail.com"))).id
			val member2 = memberRepository.insert(memberFixture(email = Email("aa2@gmail.com"))).id
			val member3 = memberRepository.insert(memberFixture(email = Email("aa3@gmail.com"))).id

			studyGroupRepository.insert(
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(member1, StudyMemberRole.LEADER),
						),
				),
			)

			studyGroupRepository.insert(
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(member2, StudyMemberRole.LEADER),
						),
				),
			)

			studyGroupRepository.insert(
				studyGroupFixture(
					studyMembers =
						mutableSetOf(
							StudyMember(member2, StudyMemberRole.LEADER),
							StudyMember(member3, StudyMemberRole.MEMBER),
						),
				),
			)

			queryAdapter.countBy(CountStudyGroupCondition()) shouldBe 3
			queryAdapter.countBy(CountStudyGroupCondition(memberId = member1)) shouldBe 1
			queryAdapter.countBy(CountStudyGroupCondition(memberId = member2)) shouldBe 2
			queryAdapter.countBy(CountStudyGroupCondition(memberId = member3)) shouldBe 1
		}
	})