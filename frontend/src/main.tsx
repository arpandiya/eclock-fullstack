import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { RouterProvider } from '@tanstack/react-router'
import {routeTree} from './routeTree.gen.ts'

import { createRouter } from '@tanstack/react-router'
import './index.css'
import App from './App.tsx'



const router = createRouter({
  routeTree,
});

declare module '@tanstack/react-router' {
  interface RegisterRouter {
    router: typeof router;
  }
}

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
