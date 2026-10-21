import fs from 'node:fs';
import path from 'node:path';

const DOCS_DIR = path.resolve('../docs');
const TARGET_DIR = path.resolve('./src/content/docs');

if (!fs.existsSync(TARGET_DIR)) {
	fs.mkdirSync(TARGET_DIR, { recursive: true });
}

const modules = [
	{
		source: '01. Core Kotlin Concepts.md',
		target: 'core-kotlin.md',
		title: '01. Core Kotlin Concepts',
		description: 'Modern backend Kotlin fundamentals: Syntax, Expressions, Control Flow, and Object-Oriented Design.',
		slug: 'core-kotlin',
	},
	{
		source: '02. Null Safety and Advanced Features.md',
		target: 'null-safety.md',
		title: '02. Null Safety & Domain Modeling',
		description: 'Compile-time null safety, Scope Functions, Data Classes, Sealed Hierarchies, and Value Classes.',
		slug: 'null-safety',
	},
	{
		source: '03. Generics Delegation and Collections.md',
		target: 'generics-delegation.md',
		title: '03. Generics, Delegation & Collections',
		description: 'Declaration-site variance (in/out), Reified types, Standard Library Delegation, and Lazy Sequences.',
		slug: 'generics-delegation',
	},
	{
		source: '04. JVM Interoperability.md',
		target: 'jvm-interop.md',
		title: '04. Polyglot JVM Interoperability',
		description: 'Two-way Java/Kotlin interop, Annotation Use-Site Targets, SAM interfaces, and Bytecode Plugins.',
		slug: 'jvm-interop',
	},
	{
		source: '05. Coroutines and Structured Concurrency.md',
		target: 'coroutines-concurrency.md',
		title: '05. Coroutines & Structured Concurrency',
		description: 'Suspending functions, Structured Concurrency, Dispatchers, Cooperative Cancellation, and MDC Context.',
		slug: 'coroutines-concurrency',
	},
	{
		source: '06. Asynchronous Streams and Kotlin Flow.md',
		target: 'async-flow.md',
		title: '06. Reactive Streams with Kotlin Flow',
		description: 'Reactive non-blocking data pipelines with Kotlin Flow, Channels, Type-Safe Builders, and kotlinx ecosystem.',
		slug: 'async-flow',
	},
];

// Clean up any stale or obsolete files in TARGET_DIR
const validTargets = new Set(modules.flatMap(m => [m.target, m.target.replace('.md', '.mdx')]));
if (fs.existsSync(TARGET_DIR)) {
	for (const file of fs.readdirSync(TARGET_DIR)) {
		if (file.endsWith('.md') && !validTargets.has(file)) {
			console.log(`Removing obsolete file: ${file}`);
			fs.unlinkSync(path.join(TARGET_DIR, file));
		}
	}
}

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
