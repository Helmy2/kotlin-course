// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

export default defineConfig({
	site: 'https://helmy.is-a.dev',
	base: '/kotlin-course/',
	integrations: [
		starlight({
			title: 'Kotlin for Backend Engineering',
			description: 'Hands-on interactive master course on Kotlin 2.x, Java Interop, Spring Boot, and Coroutines.',
			social: [
				{ icon: 'github', label: 'GitHub', href: 'https://github.com/Helmy2/kotlin-course' },
			],
			customCss: ['./src/styles/custom.css'],
			components: {
				Header: './src/components/CustomHeader.astro',
				PageTitle: './src/components/CustomPageTitle.astro',
				Footer: './src/components/CustomFooter.astro',
			},
			sidebar: [
				{
					label: 'Course Overview',
					items: [
						{ label: 'Introduction & Curriculum', slug: 'index' },
					],
				},
				{
					label: 'Course Topics (01–06)',
					items: [
						{ label: '01. Core Kotlin Concepts', slug: 'core-kotlin' },
						{ label: '02. Null Safety & Domain Modeling', slug: 'null-safety' },
						{ label: '03. Generics, Delegation & Collections', slug: 'generics-delegation' },
						{ label: '04. Polyglot JVM Interoperability', slug: 'jvm-interop' },
						{ label: '05. Coroutines & Concurrency', slug: 'coroutines-concurrency' },
						{ label: '06. Reactive Streams & Flow', slug: 'async-flow' },
					],
				},
				{
					label: 'Reference & Hands-on',
					items: [
						{ label: 'Branch Catalog (18 Branches)', slug: 'reference/branches' },
						{ label: 'Kotlin Backend Master Index', slug: 'reference/master-index' },
						{ label: 'Coroutines & Concurrency Guide', slug: 'reference/coroutine-wizard' },
					],
				},
			],
		}),
	],
});
