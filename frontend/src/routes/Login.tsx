import { createFileRoute } from '@tanstack/react-router'
import LoginComponent from '../component/LoginComponent';

export const Route = createFileRoute('/Login')({
  component: LoginComponent,
})

function Login() {
  return <LoginComponent />;
}
export default Login;