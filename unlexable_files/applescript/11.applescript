on run argv
	tell application "Google Chrome"
		-- Open incognito window
		set W to every window whose mode is "incognito"
		if W = {} then set W to {make new window with properties {mode:"incognito"}}
		set [W] to W

		-- Open new tab with incognito query
		set userQuery to item 1 of argv
		set incognitoQuery to "https://www.google.com/search?q=" & userQuery
		tell W to set T to make new tab with properties {URL:incognitoQuery}

		-- Close empty tab if found
		-- Script creates an empty tab when opening an incognito window
		set emptyTab to my lookupTabWithUrl("chrome://newtab/")
		if emptyTab is not null then
			close emptyTab
		end if

		-- Focus incognito window
		set index of W to 1
		activate
	end tell
end run

-- Function:
-- Look up tab with given URL
-- Returns tab reference, if found
to lookupTabWithURL(www)
	local www

	tell application "Google Chrome"
		set _T to a reference to (every tab of every window whose URL is www)

		-- Check if tab with given URL was found
		if (count _T) = 0 then return null
		-- Returns tab reference if found

		return item 1 of item 1 of _T
	end tell
end lookupTabWithURL