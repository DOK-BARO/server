package kr.kro.dokbaro.server.core.member.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberQueryRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.MemberDao
import java.util.UUID

@PersistenceAdapterTest
class MemberPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberMapper = MemberMapper()
		val memberRepository = MemberRepository(dslContext, memberMapper)
		val memberQueryRepository = MemberQueryRepository(dslContext, memberMapper)

		val adapter = MemberPersistenceAdapter(memberRepository, memberQueryRepository)

		val memberDao = MemberDao(configuration)

		afterEach {
			dslContext.rollback()
		}
		val uuid = UUID.randomUUID()
		val email = "hello@gmail.com"
		val member =
			Member(
				nickName = "nickname",
				email = Email(email),
				profileImage = "image.png",
				certificationId = uuid,
			)

		"저장을 수행한다" {
			val savedMember = adapter.save(member)

			savedMember.id shouldNotBe null
			savedMember.roles.shouldNotBeEmpty()
			savedMember.nickName shouldBe member.nickName
			savedMember.email.address shouldBe member.email.address
			savedMember.profileImage shouldBe member.profileImage
			savedMember.certificationId shouldBe uuid
		}

		"업데이트를 수행한다" {
			val savedMember = adapter.save(member)
			val targetMember =
				Member(
					nickName = "newNickName",
					email = Email("newEmail@gmail.com"),
					profileImage = "new.png",
					certificationId = savedMember.certificationId,
					id = savedMember.id,
				)

			adapter.update(targetMember)

			val result = memberDao.findById(targetMember.id)!!

			result.nickname shouldBe targetMember.nickName
			UUIDUtils.byteArrayToUUID(result.certificationId) shouldBe targetMember.certificationId
		}

		"certificationId를 통한 조회를 수행한다" {
			val savedMember = adapter.save(member)

			val result: Member = adapter.findByCertificationId(savedMember.certificationId)!!

			result shouldBe savedMember
		}

		"email 등록 여부를 확인한다" {
			val savedMember = adapter.save(member)

			adapter.existByEmail(savedMember.email.address) shouldBe true
			adapter.existByEmail("aaaa@koko.com") shouldBe false
		}
	})