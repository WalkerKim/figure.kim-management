<script>
    import {page} from '$app/stores';
    import {fetchWithMethod, getDataWithUrl} from "$lib/common.js";
    import {goto} from "$app/navigation";
    // import logo from '%sveltekit.assets%/favicon/228w/favicon.png';
</script>

<header>
    <div class="text-gray-500 body-font pb-1 h-16 fixed bg-custom-black-900 w-full z-20 h-20">
        <div class="mx-auto flex flex-wrap sm:px-5 px-0 sm:flex-row items-center">
            <a href="/" class="flex my-5 title-font font-medium items-center text-gray-900 cursor-pointer sm:pl-0 pl-5">
                <img src="/favicon/228w/favicon.png" alt="SvelteKit" class="max-h-10 bg-white"/>
                <span class="flex sm:hidden md:flex ml-3 lg:text-xl text-white">Figuer.kim</span><span
                    class="flex sm:hidden md:flex ml-3 lg:text-xl text-blue-400">Manager</span>
            </a>
            <nav class="z-50 mr-auto sm:ml-2 sm:pl-4 sm:border-gray-s00 sm:flex sm:flex-wrap items-center text-base justify-center w-auto">
                <div class="mt-5 sm:mt-0 mb-0 sm:-mb-0.5 sm:w-auto w-full sm:block sm:relative fixed left-0 bottom-0">
                    <ul>
                        <li
                            class="-mr-1 sm:mr-5 sm:hover:text-gray-300 cursor-pointer my-0 sm:my-1 border-t-transparent py-5 sm:px-2 sm:w-auto w-1/2 sm:inline inline-block text-center
                                    {$page.url.pathname.startsWith('/post')  ? 'sm:border-b-2 sm:border-blue-400 text-white hover:text-blue-300 sm:text-blue-400 mb-0 bg-blue-400 sm:bg-opacity-0 ':'bg-gray-800 sm:bg-opacity-0'}
                            ">
                            <a href="/post">Post</a>
                        </li>
                        <li
                            class="-mr-1 sm:mr-5 sm:hover:text-gray-300 cursor-pointer my-0 sm:my-1 border-t-transparent py-5 sm:px-2 sm:w-auto w-1/2 sm:inline inline-block text-center
                                    {$page.url.pathname.startsWith('/category')  ? 'sm:border-b-2 sm:border-blue-400 text-white hover:text-blue-300 sm:text-blue-400 mb-0 bg-blue-400 sm:bg-opacity-0 ':'bg-gray-800 sm:bg-opacity-0'}
                            ">
                            <a href="/category">Category</a>
                        </li>
                        <li
                            class="-mr-1 sm:mr-5 sm:hover:text-gray-300 cursor-pointer my-0 sm:my-1 border-t-transparent py-5 sm:px-2 sm:w-auto w-1/2 sm:inline inline-block text-center
                                    {$page.url.pathname === '/tag' ? 'sm:border-b-2 sm:border-blue-400 text-white hover:text-blue-300 sm:text-blue-400 mb-0 bg-blue-400 sm:bg-opacity-0 ':'bg-gray-800 sm:bg-opacity-0'}
                            ">
                            <a href="/tag">Tag</a>
                        </li>
                        <li
                                class="-mr-1 sm:mr-5 sm:hover:text-gray-300 cursor-pointer my-0 sm:my-1 border-t-transparent py-5 sm:px-2 sm:w-auto w-1/2 sm:inline inline-block text-center" on:click={()=>fetchWithMethod("GET","/logout").then(()=> goto("/login"))}>
                            <a href="/tag">Logout</a>
                        </li>
                    </ul>
                </div>
            </nav>
        </div>
    </div>
</header>

<style>

</style>
