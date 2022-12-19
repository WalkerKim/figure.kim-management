import {sveltekit} from '@sveltejs/kit/vite';

const config = {
	plugins: [sveltekit()],
	server:{
		port:5174
		,
		proxy:{
			'/assets': {
				target: 'http://localhost:8888'
			}
		},
	},
	optimizeDeps: {
		include: ['tui-editor-svelte']
	}
};

export default config;
