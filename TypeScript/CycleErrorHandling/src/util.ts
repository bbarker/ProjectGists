import xs, {Listener, Stream} from 'xstream';
import {Response} from "@cycle/http";

import {isNullOrUndefined} from "util";
export function isErrorResponse(resp: Response): boolean {
    if (isNullOrUndefined(resp.text)) {
        console.log(JSON.stringify(resp));
    }
    return isNullOrUndefined(resp.text)
}


export interface WrappedResponse {
    success$: Stream<Response>;
    error$: Stream<Response>
}

interface IsError<T> {
    val: T
    isError: boolean
}

export function wrapHTTP(http$$: Stream<Stream<Response>>): WrappedResponse {
    const unsafeHtttp$$: Stream<IsError<Stream<Response>>> =
        http$$.map(mr => {return {val: mr, isError: false}});
    const metaResponse$$: Stream<IsError<Stream<Response>>> = unsafeHtttp$$.replaceError(err => {
        console.log(`Error in meta response: ${err}`);
        err.isError = true;
        return xs.of(err);
    });

    const metaSuccess$$ = metaResponse$$.filter(mrWrap => !mrWrap.isError).map(mrWrap => mrWrap.val);
    const metaError$$: Stream<Stream<Response>> = metaResponse$$.filter(mrWrap => mrWrap.isError).map(mrWrap => mrWrap.val);

    const response$ = metaSuccess$$.map(res => res.replaceError(err => {
        console.log(`Error in response: ${err}`);
        return xs.of(err);
    })).flatten();

    response$.setDebugListener(makeErrorListener('response$ in wrapHTTP'));

    const success$ = response$.filter(resp => !isErrorResponse(resp));
    success$.setDebugListener(makeErrorListener('success$ in wrapHTTP'));
    const innerError$ = response$.filter(resp => isErrorResponse(resp));
    innerError$.setDebugListener(makeErrorListener('innerError$ in wrapHTTP'));

    const error$ = xs.merge(metaError$$.flatten(), innerError$);
    return {
        success$,
        error$
    };
}

/*
 Primarily intended for debugging
 */
export function makeErrorListener(message: string): Partial<Listener<any>> {
    return {
        next: val => console.log(`logging value {${val}}: ${message}`),
        error: val => console.log(`logging error {${val}}: ${message}`),
        complete: () => console.log(`complete: ${message}`)
    }
}