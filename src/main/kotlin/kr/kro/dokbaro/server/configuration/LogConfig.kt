package kr.kro.dokbaro.server.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.context.annotation.Configuration

private val log = KotlinLogging.logger { }

@Aspect
@Configuration
class LogConfig {
	@Around(
		"""
		execution(* kr.kro.dokbaro.server.domain..*(..)) &&
			 !execution(* kr.kro.dokbaro.server.domain..adapter.input..*(..))
		""",
	)
	fun debugLog(joinPoint: ProceedingJoinPoint): Any? {
		log.debug { "===> ${toRequestFormat(joinPoint)}" }
		val result = joinPoint.proceed()
		log.debug { "<=== ${toResultFormat(joinPoint, result)}" }

		return result
	}

	@Around("execution(* kr.kro.dokbaro.server.domain..adapter.input..*(..))")
	fun infoLog(joinPoint: ProceedingJoinPoint): Any? {
		log.info { "===> ${toRequestFormat(joinPoint)}" }
		val result = joinPoint.proceed()
		log.info { "<=== ${toResultFormat(joinPoint, result)}" }

		return result
	}

	@AfterThrowing("execution(* kr.kro.dokbaro.server.domain..*(..))", throwing = "ex")
	fun clientErrorLog(
		joinPoint: JoinPoint,
		ex: Exception,
	) {
		log.error { "<===X=== ${toRequestFormat(joinPoint)} --error-- ${ex.message}" }
	}

	private fun toRequestFormat(joinPoint: JoinPoint) =
		"${toMethodFormat(joinPoint)} ( ${joinPoint.args.map { it.toString() }} )"

	private fun toResultFormat(
		joinPoint: JoinPoint,
		result: Any?,
	) = "${toMethodFormat(joinPoint)} ${result ?: ""}"

	private fun toMethodFormat(joinPoint: JoinPoint) =
		"${joinPoint.signature.declaringTypeName.split('.').last()}.${joinPoint.signature.name}"
}