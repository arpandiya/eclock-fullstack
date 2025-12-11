import { createRootRoute, Link, Outlet } from '@tanstack/react-router'
import { TanStackRouterDevtools } from '@tanstack/react-router-devtools'
import Nav from '../component/Nav'


export const Route = createRootRoute({
  component: () => (
    <>
     <nav><Nav /></nav>
      <Outlet />
      <TanStackRouterDevtools />
    </>
  ),
})