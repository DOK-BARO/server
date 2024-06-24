package kr.kro.dokbaro.server.template.port

interface SavePort<C, R> {
	fun save(command: C): R
}