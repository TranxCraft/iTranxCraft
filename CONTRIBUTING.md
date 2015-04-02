# Contributing to iTranxCraft

iTranxCraft is a CraftBukkit server plugin designed primarily to support the [TranxCraft](https://www.tranxcraft.com/) server. However, it can be used in a variety of other configurations with some fuss.

For those who wish to contribute, we encourage you to fork the repository and submit pull requests. Below you will find guidelines that will explain this process in further detail.

## Quick Guide

1. Create or find an issue on the [issue tracker](https://github.com/TranxCraft/iTranxCraft/issues) and check to see if the issue has been assigned to anyone. If it's not assigned to anyone, you can work on it.
2. Is your change suitable for this server?
3. Fork iTranxCraft if you haven't done so already.
4. Make a branch dedicated to your change.
5. Make your change.
6. Commit your change according to the [commiting guidelines](#committing-your-changes).
7. Push your branch and submit a pull request.

## Getting Started

* Search the [issue tracker](https://github.com/TranxCraft/iTranxCraft/issues) for your bug report or feature request.
* If the issue does not exist already, create it.
    * Clearly describe the issue.
    * If your issue is a bug, describe the steps needed to reproduce it.
    * If your issue is a feature request, ensure it fits the genre of the server and describe the feature in detail.
* Fork the repository on GitHub.

## Does the change reflect the server genre?
As a rough guideline, ask yourself the following questions to determine whether your proposed change fits the server genre. Please remember that this is only a rough guideline and may or may not reflect the definitive answer to this question.

* Is the change in line with the server genre? TranxCraft is a survival and survival games based server. If your change is suited for a creative or freedom based server it most likely won't be implemented.
* Is there a similar feature already in place on the server? If so, the request will most likely not be implemented.

## Making Changes

* Create a topic branch from where you want to base your work.
  * This is usually the master branch.
  * Name your branch something relevant to the change you are going to make.
  * To quickly create a topic branch based on master, use `git checkout master` followed by `git checkout -b <name>`. Avoid working directly on the master branch.
* Make **_sure_** your change meets our [code requirements](#code-requirements).

## Code Requirements

* Code must be written in [Stroustrup style](http://en.wikipedia.org/wiki/Indent_style#Variant:_Stroustrup) and  it follows the [Java Code Conventions](http://www.oracle.com/technetwork/java/codeconventions-150003.pdf).
* No trailing whitespace for code lines, comments, or configuration files.
* No CRLF line endings, only LF is allowed.
  * For Windows-based machines, you can configure Git to do this for you by running `git config --global core.autocrlf true`.
  * If you're running Linux/OS X, you should run `git config --global core.autocrlf input` instead.
  * For more information about line feeds, see [this article](http://adaptivepatchwork.com/2012/03/01/mind-the-end-of-your-line/).
* No 80 character line limit or 'weird' mid-statement newlines.
* Additions must be compiled, complete and tested before commiting.
* Avoid using `org.bukkit.Server.dispatchCommand()` or anything similar. Commits that make use of this will most likely be denied.
* Files must always end with a newline.
* Avoid nested code structures.

## Commiting Your Changes

* Check for unnecessary whitespace with `git diff --check` before commiting.
* Describe your changes in the commit description.
* For a prolonged description, continue on a new line.
* The first description line should be one sentence and should not exceed 10 words.
* The first description line should contain either:
  * For a bug-related issue: "Resolves *#issue*".
  * For a feature request: "Implements *#issue*".
  * "*#issue*" is the issue number you based your work on.
  
#### Example Commit message
```
Fixed the ModuleLoader. Resolves #66
There was an issue with the ModuleLoader loading external modules. This was due to the external module not 
being loaded into memory. Through the use of a library that issue has been resolved.
```

## Submitting Your Changes

* Push your changes to the topic branch in your fork of the repository.
* Submit a pull request to this repository.
  * Explain in detail what each one of your commits changes and point out any big changes.
* Wait as an official developer evaluates your changes.

## Tips - How To Get Your Pull Request Accepted

* Please make sure your changes are written like other features would be. For example: Commands have their own class and extend BukkitCommand.
* Do not increment the version number.
* If you want to add multiple changes, please make one pull request per change. This way, it's easier to accept your changes faster and won't block the other changes if there is an issue with a specific line of code.
* Please avoid having to add files main namespace where possible.
* Please refrain from using an excessive amount of commits. As few as possible is generally the best.
* Please do not spread your contribution over several pull-requests.
 

## I'm interested in becoming an 'official' developer. How?

If you wish to become an official developer, check out the thread on our forums [here](https://www.tranxcraft.com/forums/index.php?/topic/61-how-to-become-an-official-developer/): https://www.tranxcraft.com/forums/index.php?/topic/61-how-to-become-an-official-developer/

## Additional Resources

* [TranxCraft information](https://www.tranxcraft.com)
* [TranxCraft forums](https://www.tranxcraft.com/forums)
* [Issue tracker](https://github.com/TranxCraft/iTranxCraft/issues)
* [General GitHub documentation](http://help.github.com/)
* [GitHub pull request documentation](http://help.github.com/send-pull-requests/)
