<script>
	import Header from '../Header.svelte';
	import '../styles.css';
	import {onDestroy, onMount, beforeUpdate} from "svelte"
	import {fetchWithMethod} from "$lib/common.js";
	import {goto} from "$app/navigation";

	let isLogin = false;

	onMount(()=>{

	})
	onDestroy(()=>{
		console.log("onDestroy");
	})
	beforeUpdate(()=>{
		console.log("beforeUpdate");
		fetchWithMethod("GET","/session-check").then(res=>{
			if(res.status==401){
				if(confirm("로그인 정보가 없습니다. 로그인 페이지로 가실?")){
					goto("/login")
					return
				}
			}else{
				isLogin = true;
				// return res
			}
		})
		console.log("onMount");
	})


</script>
<style  global>
	@import "../styles.css";
</style>
<div class="app {isLogin?'':'hidden'}" id="app-div">
	<Header />

	<div class="container mx-auto">
		<main class="w-full border-t border-gray-400 -mt-0 pt-8">
			<div class="px-4 sm:px-8 pb-20 sm:pb-0 mt-16">
				<slot />
			</div>
		</main>
	</div>
</div>

