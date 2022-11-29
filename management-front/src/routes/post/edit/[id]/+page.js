import { error } from '@sveltejs/kit';

/** @type {import('./$types').PageLoad} */
export function load({ params }) {
    console.log(params)
    //TODO get post from server
    let post;
    post ??= {
            title: 'Hello world!',
            content: '# test h1 \n ## Test h2'
        };
    console.log(post);
    return post;
    throw error(404, 'Not found');
}