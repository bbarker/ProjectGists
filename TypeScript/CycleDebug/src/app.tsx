import xs, { Stream } from 'xstream';
import { VNode, DOMSource } from '@cycle/dom';
import { StateSource } from 'cycle-onionify';
import {Response} from "@cycle/http";

import { Sources, Sinks } from './interfaces';
import {HTTPSource} from "@cycle/http";

export type AppSources = Sources & { onion : StateSource<AppState> };
export type AppSinks = Sinks & { onion : Stream<Reducer> };
export type Reducer = (prev : AppState) => AppState;
export type AppState = {
    count : number;
    text: string;
};

export function App(sources : AppSources) : AppSinks
{
    const action$: Stream<Reducer> = intent(sources.DOM);
    const httpData$: Stream<Reducer> = model(sources.HTTP);
    const model$ = xs.merge(action$, httpData$);
    const vdom$: Stream<VNode> = view(sources.onion.state$);

    const request$ = xs.of({
        url: 'http://example.com/', // GET method by default
        category: 'hello',
    });

    return {
        DOM: vdom$,
        HTTP: request$,
        onion: model$
    };
}


function model(HTTP: HTTPSource): Stream<Reducer> {
    const response$: Stream<Response> = HTTP.select('hello').flatten();
    return response$.map<Reducer>(resp =>
        (state) =>({ ...state, text: resp.text })
    );
}

function intent(DOM : DOMSource) : Stream<Reducer>
{
    const init$ : Stream<Reducer> = xs.of<Reducer>(() => ({ text: '', count: 0 }));

    const add$ : Stream<Reducer> = DOM.select('.add').events('click')
        .mapTo<Reducer>(state => ({ ...state, count: state.count + 1 }));


    //
    // In this case, if we catch the error early, replaceError is called
    // and the application proceeds to function. However, if instead we throw the
    // error in the mapTo stream, and then call replaceError, it doesn't work
    //
    // This works:
    // const subtract$ : Stream<Reducer> = DOM.select('.subtract').events('click')
    //     .map( (x) => { throw 'fake intent error'; return x})
    //     .replaceError(err => {
    //         console.log(`Error subtract: ${err}`);
    //         return xs.of(err);
    //     }).mapTo<Reducer>(state => ({ ...state, count: state.count - 1 }));

    // This does not work:
    const subtract$ : Stream<Reducer> = DOM.select('.subtract').events('click')
        .mapTo<Reducer>(state => {
            throw 'fake intent error';
            return ({ ...state, count: state.count - 1 });
        })
        .replaceError(err => {
            console.log(`Error subtract: ${err}`);
            return xs.of(err);
        });

    // Comment from Andre on gitter:
    // The error happens in a reducer, and the reducer is just a value
    // (a function) that you are passing around. That function is not called
    // in that operator chain, it lands inside onionify and onionify is
    // the one who calls the function that's why the error might happen in
    // the onionify stack I suspect you error could be swallowed due to the
    // imitate cycle inside onionify (internal stuff) this could actually be a real
    // bug, we should make an issue, I'll take a look at that at some point

    return xs.merge(init$, add$, subtract$);
}

function view(state$ : Stream<AppState>) : Stream<VNode>
{

    return state$
        .map(s => s.count)
        .map(count =>
            <div>
                <h2>My Awesome Cycle.js app</h2>
                <span>{ 'Counter: ' + count }</span>
                <button type='button' className='add'>Increase</button>
                <button type='button' className='subtract'>Decrease</button>
            </div>
        );
}
