import {sveltekit} from '@sveltejs/kit/vite';
import { loadEnv } from 'vite';
export default ({mode})=>{
	process.env = {...process.env, ...loadEnv(mode, process.cwd())};
	const config = {
		plugins: [sveltekit()],
		server:{
			port:5174
			,
			proxy:{
				'/assets': {
					target: process.env.VITE_BACKEND_ADDR
				}
			},
		},
		optimizeDeps: {
			include: ['tui-editor-svelte']
		}
	};
	return config;
}


