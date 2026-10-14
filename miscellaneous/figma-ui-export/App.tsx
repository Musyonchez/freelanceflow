import { useEffect, useMemo, useState } from "react"

type IconName = "arrow" | "briefcase" | "calendar" | "check" | "chevron" | "clock" | "close" | "file" | "home" | "invoice" | "menu" | "pause" | "people" | "plus" | "receipt" | "wallet"

const iconPaths: Record<IconName, React.ReactNode> = {
  arrow: <path d="m9 18 6-6-6-6" />,
  briefcase: (
    <>
      <rect x="3" y="7" width="18" height="13" rx="2" />
      <path d="M8 7V5a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M3 12h18M10 12v2h4v-2" />
    </>
  ),
  calendar: (
    <>
      <rect x="3" y="5" width="18" height="16" rx="2" />
      <path d="M16 3v4M8 3v4M3 10h18" />
    </>
  ),
  check: <path d="m5 12 4 4L19 6" />,
  chevron: <path d="m9 18 6-6-6-6" />,
  clock: (
    <>
      <circle cx="12" cy="12" r="9" />
      <path d="M12 7v5l3 2" />
    </>
  ),
  close: <path d="m6 6 12 12M18 6 6 18" />,
  file: (
    <>
      <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8Z" />
      <path d="M14 2v6h6M8 13h8M8 17h5" />
    </>
  ),
  home: (
    <>
      <path d="m3 11 9-8 9 8" />
      <path d="M5 10v10h14V10M9 20v-6h6v6" />
    </>
  ),
  invoice: (
    <>
      <path d="M6 2h12v20l-3-2-3 2-3-2-3 2Z" />
      <path d="M9 8h6M9 12h6M9 16h3" />
    </>
  ),
  menu: <path d="M4 7h16M4 12h16M4 17h16" />,
  pause: (
    <>
      <path d="M9 6v12M15 6v12" />
    </>
  ),
  people: (
    <>
      <circle cx="9" cy="8" r="3" />
      <path d="M3 20v-2a6 6 0 0 1 12 0v2M16 4a4 4 0 0 1 0 8M17 14a5 5 0 0 1 4 5v1" />
    </>
  ),
  plus: <path d="M12 5v14M5 12h14" />,
  receipt: (
    <>
      <path d="M5 3v18l3-2 4 2 4-2 3 2V3l-3 2-4-2-4 2Z" />
      <path d="M9 9h6M9 13h6" />
    </>
  ),
  wallet: (
    <>
      <path d="M4 6a2 2 0 0 1 2-2h13v16H6a2 2 0 0 1-2-2Z" />
      <path d="M4 8h15M15 12h6v5h-6a2.5 2.5 0 0 1 0-5Z" />
    </>
  ),
}

function Icon({ name, size = 20 }: { name: IconName size?: number }) {
  return (
    <svg
      aria-hidden="true"
      fill="none"
      height={size}
      viewBox="0 0 24 24"
      width={size}
    >
      <g
        stroke="currentColor"
        strokeLinecap="round"
        strokeLinejoin="round"
        strokeWidth="1.8"
      >
        {iconPaths[name]}
      </g>
    </svg>
  )
}

function Avatar({
  initials,
  tone = "mint",
}: {
  initials: string
  tone?: string
}) {
  return <span className={`avatar avatar-${tone}`}>{initials}</span>
}

function Tag({
  children,
  tone = "neutral",
}: {
  children: React.ReactNode
  tone?: string
}) {
  return <span className={`tag tag-${tone}`}>{children}</span>
}

const navigation: { id: string label: string icon: IconName }[] = [
  { id: "home", label: "Home", icon: "home" },
  { id: "work", label: "Work", icon: "briefcase" },
  { id: "money", label: "Money", icon: "wallet" },
  { id: "clients", label: "Clients", icon: "people" },
]

const projects = [
  {
    client: "Northstar Labs",
    color: "violet",
    progress: 72,
    project: "Mobile redesign",
    value: "36:20 h",
  },
  {
    client: "Alba Studio",
    color: "amber",
    progress: 45,
    project: "Brand system",
    value: "18:45 h",
  },
  {
    client: "Nook & Co.",
    color: "mint",
    progress: 86,
    project: "E-commerce build",
    value: "51:10 h",
  },
]

const invoices = [
  {
    client: "Northstar Labs",
    code: "INV-024",
    due: "Due 18 Jun",
    amount: "$2,480.00",
    status: "Sent",
    tone: "blue",
  },
  {
    client: "Alba Studio",
    code: "INV-023",
    due: "Due today",
    amount: "€1,260.00",
    status: "Due",
    tone: "amber",
  },
  {
    client: "Nook & Co.",
    code: "INV-021",
    due: "Paid 10 Jun",
    amount: "£3,850.00",
    status: "Paid",
    tone: "green",
  },
]

export default function App() {
  const [activeTab, setActiveTab] = useState("home")
  const [timerRunning, setTimerRunning] = useState(true)
  const [seconds, setSeconds] = useState(7264)
  const [sheetOpen, setSheetOpen] = useState(false)

  useEffect(() => {
    if (!timerRunning) return
    const timer = window.setInterval(
      () => setSeconds((value) => value + 1),
      1000,
    )
    return () => window.clearInterval(timer)
  }, [timerRunning])

  const time = useMemo(() => {
    const hours = Math.floor(seconds / 3600)
      .toString()
      .padStart(2, "0")
    const minutes = Math.floor((seconds % 3600) / 60)
      .toString()
      .padStart(2, "0")
    const secs = (seconds % 60).toString().padStart(2, "0")
    return `${hours}:${minutes}:${secs}`
  }, [seconds])

  const workspace = {
    work: {
      eyebrow: "3 active · 1 upcoming",
      title: "Projects & time",
      summary: "106.3 hours",
      detail: "76% of your June target",
      icon: "briefcase" as IconName,
      items: projects.map((project) => ({
        title: project.project,
        meta: `${project.client} · ${project.progress}% complete`,
        value: project.value,
        tone: project.color,
      })),
    },
    money: {
      eyebrow: "Multi-currency overview",
      title: "Invoices & expenses",
      summary: "$3,740 due",
      detail: "$1,940 expenses this month",
      icon: "wallet" as IconName,
      items: invoices.map((invoice) => ({
        title: invoice.client,
        meta: `${invoice.code} · ${invoice.due}`,
        value: invoice.amount,
        tone: invoice.tone,
      })),
    },
    clients: {
      eyebrow: "8 clients · 3 active",
      title: "Client directory",
      summary: "$12,580 earned",
      detail: "Across 3 currencies this month",
      icon: "people" as IconName,
      items: [
        {
          title: "Northstar Labs",
          meta: "United States · USD",
          value: "$6,840",
          tone: "violet",
        },
        {
          title: "Alba Studio",
          meta: "Spain · EUR",
          value: "€2,760",
          tone: "amber",
        },
        {
          title: "Nook & Co.",
          meta: "United Kingdom · GBP",
          value: "£3,850",
          tone: "mint",
        },
      ],
    },
  }[(activeTab as "work" | "money" | "clients")]

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand-mark">F</div>
        <div className="side-nav">
          {navigation.map((item) => (
            <button
              aria-label={item.label}
              className={`side-nav-item ${
                activeTab === item.id ? "active" : ""
              }`}
              key={item.id}
              onClick={() => setActiveTab(item.id)}
              type="button"
            >
              <Icon name={item.icon} />
              <span>{item.label}</span>
            </button>
          ))}
        </div>
        <Avatar initials="DH" tone="dark" />
      </aside>

      <main className="main-content">
        <header className="topbar">
          <div>
            <p className="eyebrow">Friday, 9 October</p>
            <h1>
              {activeTab === "home"
                ? "Good evening, Daleen"
                : navigation.find((item) => item.id === activeTab)?.label}
            </h1>
          </div>
          <button className="profile-button" type="button">
            <Avatar initials="DH" tone="dark" />
            <span className="profile-copy">
              <strong>Daleen Hunnipher</strong>
              <small>Independent designer</small>
            </span>
            <Icon name="chevron" size={16} />
          </button>
        </header>

        {activeTab === "home" && (
          <div className="dashboard">
            <section className="hero-card">
              <div className="hero-top">
                <div>
                  <p className="overline">Available balance</p>
                  <p className="balance">$8,240.50</p>
                  <p className="balance-note">
                    <span>+12.4%</span> from last month
                  </p>
                </div>
                <div className="month-chip">
                  <Icon name="calendar" size={16} /> October
                </div>
              </div>
              <div className="hero-stats">
                <div>
                  <span>Earned</span>
                  <strong>$12,580</strong>
                </div>
                <div>
                  <span>Expenses</span>
                  <strong>$1,940</strong>
                </div>
                <div>
                  <span>Tax set aside</span>
                  <strong>$2,399</strong>
                </div>
              </div>
            </section>

            <section className="timer-card">
              <div className="timer-label">
                <span className={`live-dot ${timerRunning ? "running" : ""}`} />
                {timerRunning ? "Timer running" : "Timer paused"}
              </div>
              <div className="timer-row">
                <div>
                  <h2>Northstar Labs</h2>
                  <p>Mobile redesign · Product design</p>
                </div>
                <strong className="timer-value">{time}</strong>
                <button
                  aria-label={timerRunning ? "Pause timer" : "Resume timer"}
                  className={`timer-control ${timerRunning ? "" : "paused"}`}
                  onClick={() => setTimerRunning((value) => !value)}
                  type="button"
                >
                  <Icon name={timerRunning ? "pause" : "clock"} size={22} />
                </button>
              </div>
            </section>

            <div className="section-heading">
              <div>
                <p className="overline">At a glance</p>
                <h2>Business overview</h2>
              </div>
              <button className="text-button" type="button">
                This month <Icon name="chevron" size={15} />
              </button>
            </div>

            <section className="metrics-grid">
              <article className="metric-card">
                <span className="metric-icon purple">
                  <Icon name="clock" />
                </span>
                <div>
                  <p>Billable time</p>
                  <strong>106.3 h</strong>
                  <small>of 140 h target</small>
                </div>
                <span className="metric-trend">76%</span>
              </article>
              <article className="metric-card">
                <span className="metric-icon peach">
                  <Icon name="invoice" />
                </span>
                <div>
                  <p>Outstanding</p>
                  <strong>$3,740</strong>
                  <small>2 invoices</small>
                </div>
                <span className="metric-alert">1 due</span>
              </article>
            </section>

            <div className="content-grid">
              <section className="panel projects-panel">
                <div className="panel-header">
                  <div>
                    <p className="overline">In progress</p>
                    <h2>Active projects</h2>
                  </div>
                  <button
                    className="icon-button"
                    aria-label="View projects"
                    onClick={() => setActiveTab("work")}
                    type="button"
                  >
                    <Icon name="arrow" />
                  </button>
                </div>
                <div className="project-list">
                  {projects.map((project) => (
                    <div className="project-row" key={project.project}>
                      <Avatar
                        initials={project.client.slice(0, 2).toUpperCase()}
                        tone={project.color}
                      />
                      <div className="project-copy">
                        <div>
                          <strong>{project.project}</strong>
                          <span>{project.value}</span>
                        </div>
                        <p>{project.client}</p>
                        <div className="progress-track">
                          <span style={{ width: `${project.progress}%` }} />
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </section>

              <section className="panel invoices-panel">
                <div className="panel-header">
                  <div>
                    <p className="overline">Cash flow</p>
                    <h2>Recent invoices</h2>
                  </div>
                  <button
                    className="icon-button"
                    aria-label="View invoices"
                    onClick={() => setActiveTab("money")}
                    type="button"
                  >
                    <Icon name="arrow" />
                  </button>
                </div>
                <div className="invoice-list">
                  {invoices.map((invoice) => (
                    <div className="invoice-row" key={invoice.code}>
                      <span className="document-icon">
                        <Icon name="file" />
                      </span>
                      <div className="invoice-copy">
                        <strong>{invoice.client}</strong>
                        <span>
                          {invoice.code} · {invoice.due}
                        </span>
                      </div>
                      <div className="invoice-value">
                        <strong>{invoice.amount}</strong>
                        <Tag tone={invoice.tone}>{invoice.status}</Tag>
                      </div>
                    </div>
                  ))}
                </div>
              </section>
            </div>
          </div>
        )}

        {activeTab !== "home" && (
          <section className="workspace-view">
            <div className="workspace-summary">
              <span className="empty-icon">
                <Icon name={workspace.icon} size={30} />
              </span>
              <div>
                <p className="overline">{workspace.eyebrow}</p>
                <h2>{workspace.title}</h2>
              </div>
              <button
                className="primary-button"
                onClick={() => setSheetOpen(true)}
                type="button"
              >
                <Icon name="plus" size={18} /> Add new
              </button>
            </div>
            <div className="workspace-stat">
              <p>{workspace.summary}</p>
              <span>{workspace.detail}</span>
            </div>
            <div className="panel workspace-list">
              <div className="panel-header">
                <div>
                  <p className="overline">Connected records</p>
                  <h2>Recent activity</h2>
                </div>
                <Tag tone="green">Synced</Tag>
              </div>
              {workspace.items.map((item) => (
                <button
                  className="workspace-row"
                  key={item.title}
                  type="button"
                >
                  <Avatar
                    initials={item.title.slice(0, 2).toUpperCase()}
                    tone={item.tone}
                  />
                  <span>
                    <strong>{item.title}</strong>
                    <small>{item.meta}</small>
                  </span>
                  <b>{item.value}</b>
                  <Icon name="chevron" size={17} />
                </button>
              ))}
            </div>
          </section>
        )}
      </main>

      <button
        aria-label="Create new"
        className="floating-button"
        onClick={() => setSheetOpen(true)}
        type="button"
      >
        <Icon name="plus" size={25} />
      </button>

      <nav className="bottom-nav">
        {navigation.map((item) => (
          <button
            className={activeTab === item.id ? "active" : ""}
            key={item.id}
            onClick={() => setActiveTab(item.id)}
            type="button"
          >
            <Icon name={item.icon} />
            <span>{item.label}</span>
          </button>
        ))}
      </nav>

      {sheetOpen && (
        <div className="sheet-backdrop" onClick={() => setSheetOpen(false)}>
          <section
            aria-label="Create new"
            className="action-sheet"
            onClick={(event) => event.stopPropagation()}
          >
            <div className="sheet-handle" />
            <div className="sheet-title">
              <div>
                <p className="overline">Quick create</p>
                <h2>What would you like to add?</h2>
              </div>
              <button
                aria-label="Close"
                className="icon-button"
                onClick={() => setSheetOpen(false)}
                type="button"
              >
                <Icon name="close" />
              </button>
            </div>
            <div className="action-grid">
              <button type="button">
                <span className="metric-icon purple">
                  <Icon name="clock" />
                </span>
                <strong>Time entry</strong>
                <small>Log billable hours</small>
              </button>
              <button type="button">
                <span className="metric-icon peach">
                  <Icon name="receipt" />
                </span>
                <strong>Expense</strong>
                <small>Capture a receipt</small>
              </button>
              <button type="button">
                <span className="metric-icon green">
                  <Icon name="invoice" />
                </span>
                <strong>Invoice</strong>
                <small>Bill a client</small>
              </button>
              <button type="button">
                <span className="metric-icon blue">
                  <Icon name="people" />
                </span>
                <strong>Client</strong>
                <small>Add a contact</small>
              </button>
            </div>
          </section>
        </div>
      )}
    </div>
  )
}
