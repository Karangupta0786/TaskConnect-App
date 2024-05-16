# Things to remember
// To integrate side navigation in activity: </br>
// First add DrawerLayout, Toolbar, NavigationView in XML & then.</br>

        val toolbar = findViewById<Toolbar>(R.id.toolbar) // find the toolbar
        setSupportActionBar(toolbar)

        binding.navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this,binding.drawerLayout,toolbar,R.string.open_nav,R.string.close_nav)

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
