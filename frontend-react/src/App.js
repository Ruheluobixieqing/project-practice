import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>用户管理系统 - React版本</h1>
        <p>欢迎使用React重写的用户管理系统</p>

        <div style={{ marginTop: '2rem'}}>
          <h2>功能列表</h2>
          <ul style={{ textAlign: 'left'}}>
            <li>查看用户列表</li>
            <li>添加新用户</li>
            <li>编辑用户信息</li>
            <li>删除用户</li>
          </ul>
        </div>
      </header>
    </div>
  )
}

export default App;