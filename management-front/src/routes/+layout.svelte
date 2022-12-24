<script>
    import Header from './Header.svelte';
    import './styles.css';
</script>
<style  global>
	@import "./styles.css";
</style>
<div class="app">
	<Header />

	<div class="container mx-auto">
		<main class="w-full border-t border-gray-400 -mt-0 pt-8">
			<div class="px-4 sm:px-8 pb-20 sm:pb-0 mt-16">
				<slot />
			</div>
		</main>
	</div>
</div>

