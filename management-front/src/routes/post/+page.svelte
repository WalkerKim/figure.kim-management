<script>
	import { Editor } from 'bytemd'
	import breaks from '@bytemd/plugin-breaks'
	import frontmatter from '@bytemd/plugin-frontmatter'
	import gfm from '@bytemd/plugin-gfm'
	import highlight from '@bytemd/plugin-highlight'
	import math from '@bytemd/plugin-math'
	import mediumZoom from '@bytemd/plugin-medium-zoom'
	import mermaid from '@bytemd/plugin-mermaid'
	import gemoji from '@bytemd/plugin-gemoji'
	import 'bytemd/dist/index.css'
	import 'highlight.js/styles/vs.css'
	import 'github-markdown-css' // placed after highlight styles to override `code` padding
	import 'katex/dist/katex.css'
	function stripPrefixes(obj) {
		return Object.entries(obj).reduce((p, [key, value]) => {
			p[key.split('/').slice(-1)[0].replace('.json', '')] = value;
			// console.log(p)
			return p;
		}, {});
	}
	const locales = stripPrefixes(import.meta.globEager('/node_modules/bytemd/locales/*.json'));
	const gfmLocales = stripPrefixes(import.meta.globEager('/node_modules/@bytemd/plugin-gfm/locales/*.json'));
	const mathLocales = stripPrefixes(import.meta.globEager('/node_modules/@bytemd/plugin-math/locales/*.json'));
	const mermaidLocales = stripPrefixes(import.meta.globEager('/node_modules/@bytemd/plugin-mermaid/locales/*.json'));
	let value;
	let mode = 'auto';
	let localeKey = 'en';
	let maxLength;
	let enabled = {
		breaks: false,
		frontmatter: true,
		gemoji: true,
		gfm: true,
		highlight: true,
		math: true,
		'medium-zoom': true,
		mermaid: true,
	};
	const post = {};

	$: plugins = [
		enabled.breaks && breaks(),
		enabled.frontmatter && frontmatter(),
		enabled.gemoji && gemoji(),
		enabled.gfm &&
		gfm({
			locale: gfmLocales[localeKey],
		}),
		enabled.highlight && highlight(),
		enabled.math &&
		math({
			locale: mathLocales[localeKey],
			katexOptions: { output: 'html' }, // https://github.com/KaTeX/KaTeX/issues/2796
		}),
		enabled['medium-zoom'] && mediumZoom(),
		enabled.mermaid &&
		mermaid({
			locale: mermaidLocales[localeKey],
		}),
	].filter((x) => x);
	function handleChange(e) {
		console.log(e)
		post.value = e.detail.value
		value = e.detail.value
		console.log(plugins)
	}

	function submit(e) {
		fetch( "http://localhost:13000/content", {
			method: 'POST',
			body: JSON.stringify(post

			),
			headers: {
				'Content-Type': 'application/json',
				'Origin': 'http://localhost:5000'
			},
		})
	}
</script>

<svelte:head>
	<title>About</title>
	<meta name="description" content="About this app" />
</svelte:head>

<template>
	<label for="title">Title</label><input type="text" id="title" name="titile" bind:value={post.title}>
	<label for="description">Description</label><textarea name="description" id="description" bind:value={post.description}></textarea>
	<br>
	<div class="line">
		Mode:
		{#each ['auto', 'split', 'tab'] as m}
			<label> <input type="radio" bind:group={mode} value={m} />{m}</label>
		{/each}
		, Locale:
		<select bind:value={localeKey}>
			{#each Object.keys(locales) as l}
				<option value={l}>{l}</option>
			{/each}
		</select>
		, Max length:
		<input bind:value={maxLength} type="number" />
	</div>
	<div class="line">
		Plugins:
		{#each Object.keys(enabled) as p}
			{' '}
			<label> <input type="checkbox" bind:checked={enabled[p]} />{p}</label>
		{/each}
	</div>
	<Editor
			{value}
			{mode}
			{plugins}
			{maxLength}
			placeholder={'Start writing with ByteMD'}
			locale={locales[localeKey]}
			uploadImages={(files) => {
      return Promise.all(
        files.map((file) => {
          // TODO:
          return {
            url: 'https://picsum.photos/300',
          }
        })
      )
    }}
			on:change={(e) => {
      value = e.detail.value
    }}
	/>
	<div>{post.title}</div>
	<div>{post.description}</div>

	<button on:click={submit}> sumbit</button>
</template>