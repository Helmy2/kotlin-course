import fs from 'node:fs';
import path from 'node:path';

const DOCS_DIR = path.resolve('../docs');
const TARGET_DIR = path.resolve('./src/content/docs');

if (!fs.existsSync(TARGET_DIR)) {
	fs.mkdirSync(TARGET_DIR, { recursive: true });
}

const modules = [
	{
		source: '05. Core Kotlin Concepts.md',
		target: 'core-kotlin.md',
		title: '01. Core Kotlin Concepts',
		description: 'Modern backend Kotlin fundamentals: Syntax, Expressions, Control Flow, and Object-Oriented Design.',
		slug: 'core-kotlin',
	},
	{
		source: '06. Null Safety and Advanced Features.md',
		target: 'null-safety.md',
		title: '02. Null Safety & Advanced Features',
		description: 'Compile-time null safety, Data Classes, Extension Functions, and First-Class Singletons.',
		slug: 'null-safety',
	},
	{
		source: '07. JVM Interoperability and Coroutines.md',
		target: 'jvm-coroutines.md',
		title: '03. JVM Interoperability & Coroutines',
		description: 'Two-way Java/Kotlin interop, Coroutines, Structured Concurrency, and Spring Boot Non-Blocking Aggregation.',
		slug: 'jvm-coroutines',
	},
];

for (const mod of modules) {
	// Clean up any old .mdx file with the same base name
	const oldMdx = path.join(TARGET_DIR, mod.target.replace('.md', '.mdx'));
	if (fs.existsSync(oldMdx)) {
		fs.unlinkSync(oldMdx);
	}

	const srcPath = path.join(DOCS_DIR, mod.source);
	let content = fs.readFileSync(srcPath, 'utf-8');

	// Strip initial frontmatter if width: "1600"
	if (content.startsWith('---')) {
		const secondDelim = content.indexOf('---', 3);
		if (secondDelim !== -1) {
			content = content.slice(secondDelim + 3).trim();
		}
	}

	// Normalize code fence languages to lowercase (e.g. ```Java -> ```java, ```Kotlin -> ```kotlin)
	content = content.replace(/```([A-Za-z0-9_-]+)/g, (match, lang) => {
		return '```' + lang.toLowerCase();
	});

	// Count slides
	const slideCount = content.split(/\n---\s*\n/).filter((s) => s.trim().length > 0).length;

	const md = `---
title: "${mod.title}"
description: "${mod.description}"
---

${content}
`;

	fs.writeFileSync(path.join(TARGET_DIR, mod.target), md, 'utf-8');
	console.log(`Synced ${mod.target} (${slideCount} slides)`);
}

// Ensure reference directory exists
const refDir = path.join(TARGET_DIR, 'reference');
if (!fs.existsSync(refDir)) {
	fs.mkdirSync(refDir, { recursive: true });
}

// Reference: Branches
const branchesMdx = `---
title: "Course Branch Catalog"
description: "Interactive directory of all 18 starter and solution branches across the 9 course concepts."
---

import BranchCatalog from '../../../components/BranchCatalog.astro';

Explore the starter and solution branches for each concept. Click **Copy** to grab the \`git switch\` command and jump straight into the hands-on exercise in your IDE!

<BranchCatalog />
`;
fs.writeFileSync(path.join(refDir, 'branches.mdx'), branchesMdx, 'utf-8');
console.log('Generated reference/branches.mdx');

// Reference: Master Index
const masterIndexMdx = `---
title: "Kotlin Backend Master Index"
description: "Unified matrix of Null Safety operators, Scope Functions, and JVM Interoperability annotations."
---

import PrinciplesMatrix from '../../../components/PrinciplesMatrix.astro';

This interactive index cross-references Kotlin language idioms, type system safety rules, and polyglot JVM interop annotations.

<PrinciplesMatrix />
`;
fs.writeFileSync(path.join(refDir, 'master-index.mdx'), masterIndexMdx, 'utf-8');
console.log('Generated reference/master-index.mdx');

// Reference: Coroutines & Concurrency Guide
const coroutineWizardMdx = `---
title: "Coroutines & Structured Concurrency Guide"
description: "Architectural reference for Coroutine builders, scopes, dispatchers, cancellation, and resilient error handling."
---

import PatternWizard from '../../../components/PatternWizard.astro';

When building high-concurrency backend services, asynchronous concurrency requires disciplined structure: avoid thread starvation, handle cancellations cleanly, and prevent partial failure cascading.

Browse the architectural guide below to evaluate **Scopes, Builders, Dispatchers, and Error Propagation strategies**.

<PatternWizard />
`;
fs.writeFileSync(path.join(refDir, 'coroutine-wizard.mdx'), coroutineWizardMdx, 'utf-8');
console.log('Generated reference/coroutine-wizard.mdx');
