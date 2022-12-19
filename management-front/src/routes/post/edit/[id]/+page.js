import {error} from '@sveltejs/kit';
import {getDataWithHost} from "$lib/common.js";

/** @type {import('./$types').PageLoad} */
export async function load({ params }) {
    return await getDataWithHost("/content/"+params.id);
    throw error(404, 'Not found');
}