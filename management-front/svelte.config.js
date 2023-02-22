import adapter from '@sveltejs/adapter-static';
import preprocess from "svelte-preprocess";
import 'dotenv/config';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	kit: {
		adapter: adapter({
			fallback: '200.html',
				pages: process.env.VITE_BUILD_PATH??'build',
				assets: process.env.VITE_BUILD_PATH??'build',
		},
			)
	},
	preprocess: [
		preprocess({
			postcss: true,
		}),
	],
};


export default config;
