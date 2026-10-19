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
					label: 'Core Modules (Days 1–3)',
					items: [
						{ label: '01. Core Kotlin Concepts', slug: 'core-kotlin' },
						{ label: '02. Null Safety & Advanced Features', slug: 'null-safety' },
						{ label: '03. JVM Interop & Coroutines', slug: 'jvm-coroutines' },
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
